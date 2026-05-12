package com.example.data.remote.ocr

import com.example.domain.model.ParsedPassportData

object MrzParser {

    // ── Regex patterns ───────────────────────────────────────────────────────

    /** TD3 MRZ Line 1: starts with P<, followed by 3-letter country, then names/fillers */
    private val MRZ_LINE1_REGEX = Regex("P<[A-Z0-9<]{10,}")

    /** TD3 MRZ Line 2: Document number, nationality, DOB, expiry, etc. */
    private val MRZ_LINE2_REGEX = Regex("[A-Z0-9<]{30,}")

    // ── Entry point ──────────────────────────────────────────────────────────

    fun parse(ocrText: String): ParsedPassportData {
        if (ocrText.isBlank()) {
            return ParsedPassportData(null, null, null, null, null, null, "")
        }

        // Clean lines: handle common OCR noise like '«' or dots in the MRZ
        val rawLines = ocrText
            .uppercase()
            .split("\n")
            .map { it.replace("«", "<").replace(".", "<").trim() }
            .filter { it.isNotBlank() }

        // ── 1. Try structured MRZ parsing first (most reliable) ──────────────
        val line1 = rawLines.find { it.startsWith("P<") && it.length >= 30 }
        val line2 = rawLines.find { it.matches(Regex("[A-Z0-9<]{30,}")) && !it.startsWith("P<") }

        if (line1 != null && line2 != null) {
            val mrzResult = processLines(line1, line2, ocrText)
            if (mrzResult != null) return mrzResult
        }

        // ── 2. Partial MRZ fallback
        val partialResult = tryPartialMrzParse(line1, line2, rawLines, ocrText)
        if (partialResult != null && !partialResult.lastName.isNullOrBlank()) return partialResult

        // ── 3. Full visual OCR fallback ──────────────────────────────────────
        return parseVisual(rawLines, ocrText)
    }

    // ── MRZ structured parsing ───────────────────────────────────────────────

    /**
     * Parses both MRZ lines using the TD3 passport standard layout.
     * Returns null if either line is too short or parsing throws.
     */
    private fun processLines(line1: String, line2: String, rawText: String): ParsedPassportData? {
        return try {
            if (line1.length < 44 || line2.length < 44) return null

            val (lastName, firstName) = parseNames(line1)
            ParsedPassportData(
                passportNumber = parsePassportNumber(line2),
                nationality    = parseNationality(line2),
                dateOfBirth    = parseDob(line2),
                expiryDate     = parseExpiry(line2),
                firstName      = firstName.ifBlank { null },
                lastName       = lastName.ifBlank { null },
                rawText        = rawText
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * When only one MRZ line is reliably detected, extract what we can
     * and fill the rest from visual OCR.
     */
    private fun tryPartialMrzParse(
        line1: String?,
        line2: String?,
        rawLines: List<String>,
        ocrText: String
    ): ParsedPassportData? {
        // Need at least one line
        if (line1 == null && line2 == null) return null

        var lastName: String? = null
        var firstName: String? = null
        var passportNumber: String? = null
        var nationality: String? = null
        var dob: String? = null
        var expiry: String? = null

        if (line1 != null && line1.length >= 44) {
            val names = parseNames(line1)
            lastName  = names.first.ifBlank { null }
            firstName = names.second.ifBlank { null }
        }

        if (line2 != null && line2.length >= 44) {
            passportNumber = parsePassportNumber(line2)
            nationality    = parseNationality(line2)
            dob            = parseDob(line2)
            expiry         = parseExpiry(line2)
        }

        // Fill any gaps with visual OCR
        val visual = parseVisual(rawLines, ocrText)
        return ParsedPassportData(
            passportNumber = passportNumber ?: visual.passportNumber,
            lastName       = lastName ?: visual.lastName,
            firstName      = firstName ?: visual.firstName,
            nationality    = nationality ?: visual.nationality,
            dateOfBirth    = dob ?: visual.dateOfBirth,
            expiryDate     = expiry ?: visual.expiryDate,
            rawText        = ocrText
        )
    }

    // ── Line 1 parsing ───────────────────────────────────────────────────────

    private fun parseNames(line1: String): Pair<String, String> {
        val nameSection = line1.substring(5)           // skip "P<CCC"
        val parts       = nameSection.split("<<", limit = 2)
        val lastName    = parts.getOrNull(0)?.replace('<', ' ')?.trim() ?: ""
        val firstName   = parts.getOrNull(1)?.replace('<', ' ')?.trim() ?: ""
        return Pair(lastName, firstName)
    }

    // ── Line 2 parsing ───────────────────────────────────────────────────────

    /** Chars 0–8: passport number (strip trailing filler '<') */
    private fun parsePassportNumber(line2: String): String =
        line2.substring(0, 9).trimEnd('<')

    /** Chars 10–12: nationality code */
    private fun parseNationality(line2: String): String =
        line2.substring(10, 13).trimEnd('<')

    /** Chars 13–18: date of birth YYMMDD */
    private fun parseDob(line2: String): String =
        formatMrzDate(line2.substring(13, 19))

    /** Chars 20–25: expiry date YYMMDD */
    private fun parseExpiry(line2: String): String =
        formatMrzDate(line2.substring(20, 26))

    /**
     * Converts YYMMDD → YYYY-MM-DD.
     * YY < 30 → 20YY (2000s), YY >= 30 → 19YY (1900s).
     */
    private fun formatMrzDate(yymmdd: String): String {
        val yy   = yymmdd.substring(0, 2).toInt()
        val mm   = yymmdd.substring(2, 4)
        val dd   = yymmdd.substring(4, 6)
        val yyyy = if (yy < 30) "20$yy" else "19$yy"
        return "$yyyy-$mm-$dd"
    }

    // ── Visual OCR fallback ──────────────────────────────────────────────────

    private val NAME_BLACKLIST = setOf(
        "DZA", "ALGERIE", "ALGERIENNE", "PASSPORT", "PASSEPORT", "CODE", "TYPE",
        "SURNAME", "NOM", "GIVEN", "NAMES", "PRÉNOMS", "BIRTH", "NAISSANCE",
        "EXPIRY", "EXPIRATION", "DATE", "PLACE", "LIEU", "SEX", "SEXE", "OFFICE",
        "AUTORITÉ", "AUTHORITY", "PERSONAL", "PERSONNEL", "REPUBLIQUE", "DEMOCRATIQUE",
        "SIGNATURE", "J4", "ICODE", "EYPE", "SERE", "JSEXI", "SURRNAME", "PASSP",
        "PASSPO", "PASSEPO", "PASSE", "BIRTHF", "NATIONALITY", "NATIONALITÉ",
        "JAN", "FEB", "FEV", "MAR", "APR", "AVR", "MAY", "MAI", "JUN", "JUIN",
        "JUL", "JUIL", "AUG", "AOUT", "SEP", "OCT", "NOV", "DEC"
    )

    private fun parseVisual(rawLines: List<String>, ocrText: String): ParsedPassportData {
        // Passport number — anchor to label to avoid Personal No. contamination
        val passportNumber = findNumberField(
            rawLines,
            listOf("PASSPORT NO", "PASSEPORT N", "DOCUMENT NO", "PASSPO NO"),
            exactOffset = 1
        )

        // Surname: prioritize adjacent lines (-1 or 1)
        val vSurname = findField(rawLines, listOf("SURNAME", "NOM", "SURRNAME"), exactOffset = -1)
            ?: findField(rawLines, listOf("SURNAME", "NOM", "SURRNAME"), exactOffset = 1)
            ?: findField(rawLines, listOf("SURNAME", "NOM", "SURRNAME"), exactOffset = 2)

        // Given names: prioritize adjacent lines (-1 or 1)
        val vFirstName = findField(rawLines, listOf("GIVEN", "PRÉNOMS", "NAMES"), exactOffset = -1)
            ?: findField(rawLines, listOf("GIVEN", "PRÉNOMS", "NAMES"), exactOffset = 1)
            ?: findField(rawLines, listOf("GIVEN", "PRÉNOMS", "NAMES"), exactOffset = 2)

      
        var mLastName: String? = null
        var mFirstName: String? = null
        val mrzNameLine = rawLines.find { it.startsWith("P<") }
        if (mrzNameLine != null && mrzNameLine.length > 5) {
            // Skip "P<DZA" (5 chars) and trim any extra fillers
            val nameSection = mrzNameLine.substring(5).trim('<')
            
            // Split on double << or single <
            val parts = if (nameSection.contains("<<")) {
                nameSection.split("<<", limit = 2)
            } else {
                nameSection.split("<", limit = 2)
            }
            
            // Clean function: Removes leading single-char noise like 'B' in 'B JERFI'
            fun cleanMrzName(raw: String?): String? {
                if (raw == null) return null
                val cleaned = raw.replace('<', ' ').trim()
                // If name starts with a single letter followed by space, or is just weird noise
                return if (cleaned.length > 2 && cleaned[1] == ' ' && "BSPKL".contains(cleaned[0])) {
                    cleaned.substring(2).trim()
                } else if (cleaned.startsWith("S") && cleaned.length > 3) { // Handle "SFATMA"
                    cleaned.substring(1).trim()
                } else {
                    cleaned
                }
            }

            mLastName = cleanMrzName(parts.getOrNull(0))?.takeIf { it.isNotBlank() }
            mFirstName = cleanMrzName(parts.getOrNull(1))?.takeIf { it.isNotBlank() }
        }

        // --- NAME MERGER ---
        // We compare the names from the top (Visual) and the bottom (MRZ)
        // and pick the best (longest/most complete) version. 
        // This ensures "DJERFI" is kept even if the MRZ says "B JERFI" or "JERFI".
        val finalLastName = listOfNotNull(vSurname?.trim(), mLastName?.trim())
            .filter { it.isNotBlank() }
            .maxByOrNull { it.length } ?: ""
            
        val finalFirstName = listOfNotNull(vFirstName?.trim(), mFirstName?.trim())
            .filter { it.isNotBlank() }
            .maxByOrNull { it.length } ?: ""

        // Dates
        val dob    = extractDate(ocrText, listOf("BIRTH", "NAISSANCE", "BIRTHF"))
        val expiry = extractDate(ocrText, listOf("EXPIRY", "EXPIRATION"))

        // Nationality
        val nationality = when {
            ocrText.contains("ALGERIENNE") -> "DZA"
            ocrText.contains("DZA")        -> "DZA"
            else -> findField(rawLines, listOf("NATIONALITY", "NATIONALITÉ"), exactOffset = 1) ?: "DZA"
        }

        return ParsedPassportData(
            passportNumber = passportNumber,
            lastName       = finalLastName.ifBlank { null },
            firstName      = finalFirstName.ifBlank { null },
            nationality    = nationality,
            dateOfBirth    = dob ?: "",
            expiryDate     = expiry ?: "",
            rawText        = ocrText
        )
    }

    // ── Visual field helpers ─────────────────────────────────────────────────

    /**
     * Finds a field value on the line at [exactOffset] relative to the keyword line.
     * Extracts the first token that:
     *   - is at least 2 chars long
     *   - contains only letters (names/nationality have no digits)
     *   - is not in the blacklist
     */
    private fun findField(lines: List<String>, keywords: List<String>, exactOffset: Int = 0): String? {
        val index = lines.indexOfFirst { line ->
            keywords.any { kw -> line.uppercase().contains(kw) }
        }
        if (index == -1) return null

        val offsets = if (exactOffset != 0) listOf(exactOffset) else listOf(1, 2, -1)
        for (offset in offsets) {
            val actualIdx = index + offset
            if (actualIdx !in lines.indices) continue
            val token = lines[actualIdx]
                .split(" ", "/", ":", ",")
                .map { it.trim() }
                .filter { it.length >= 2 }
                .filter { it.uppercase() !in NAME_BLACKLIST }
                .filter { it.all { c -> c.isLetter() } }
                .firstOrNull()
            if (token != null) return token
        }
        return null
    }

    /**
     * Finds a passport/document number.
     * Looks for the first token on the offset line that contains at least one digit.
     * Stops before the Personal Number field by anchoring to the label.
     */
    private fun findNumberField(lines: List<String>, keywords: List<String>, exactOffset: Int): String? {
        val index = lines.indexOfFirst { line -> keywords.any { kw -> line.contains(kw) } }
        if (index == -1 || index + exactOffset !in lines.indices) return null
        val line = lines[index + exactOffset]
        return line.split(" ", "/", ":").map { it.trim() }.find { it.any { c -> c.isDigit() } }
    }

    /**
     * Extracts a date string near a keyword label.
     * Supports:
     *   - Full dates:    "30 MAI 2004", "30 MAI/2004", "30-05-2004"
     *   - Partial dates: "MAI 2004"
     *   - French months: JAN FEV MAR AVR MAI JUIN JUIL AOUT SEP OCT NOV DEC
     *   - English months: JAN FEB MAR APR MAY JUN JUL AUG SEP OCT NOV DEC
     */
    private fun extractDate(ocrText: String, keywords: List<String>): String? {
        val lines = ocrText.uppercase().split("\n").map { it.trim() }
        val index = lines.indexOfFirst { line -> keywords.any { kw -> line.contains(kw) } }
        if (index == -1) return null

        // Search a window of ±1 line around the keyword
        val searchArea = (index - 1..index + 1)
            .filter { it in lines.indices }
            .joinToString(" ") { lines[it] }

        val monthPattern = buildString {
            append("(JAN|FEV|FEB|MAR|AVR|APR|MAI|MAY|JUIN|JUN|JUIL|JUL|AOUT|AUG|SEP|OCT|NOV|DEC|[0-9]{1,2})")
        }

        // Full date: DD <sep> MONTH <sep> YYYY
        val fullDateRegex = Regex("([0-9]{1,2})[^0-9]*$monthPattern[^0-9]*([0-9]{4})")
        fullDateRegex.find(searchArea)?.let { m ->
            return "${m.groupValues[1]} ${m.groupValues[2]} ${m.groupValues[3]}"
        }

        // Partial date: MONTH <sep> YYYY
        val partialDateRegex = Regex("$monthPattern[^0-9]*([0-9]{4})")
        partialDateRegex.find(searchArea)?.let { m ->
            return "${m.groupValues[1]} ${m.groupValues[2]}"
        }

        // Numeric fallback: DD/MM/YYYY or DD-MM-YYYY
        val numericDateRegex = Regex("([0-9]{1,2})[/\\-]([0-9]{1,2})[/\\-]([0-9]{4})")
        numericDateRegex.find(searchArea)?.let { m ->
            return "${m.groupValues[1]}/${m.groupValues[2]}/${m.groupValues[3]}"
        }

        return null
    }
}