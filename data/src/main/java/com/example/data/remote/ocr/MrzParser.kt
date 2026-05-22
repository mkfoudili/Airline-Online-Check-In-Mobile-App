package com.example.data.remote.ocr

import com.example.domain.model.ParsedPassportData

object MrzParser {

    fun parse(ocrText: String): ParsedPassportData {
        if (ocrText.isBlank()) {
            return ParsedPassportData(null, null, null, null, null, null, "")
        }

        val rawLines = ocrText
            .uppercase()
            .split("\n")
            .map { it.replace("«", "<").replace(".", "<").trim() }
            .filter { it.isNotBlank() }

        val line1 = rawLines.find { it.startsWith("P<") && it.length >= 30 }
        val line2 = rawLines.find {
            it.matches(Regex("[A-Z0-9<]{30,}")) && !it.startsWith("P<")
        }

        if (line1 != null && line2 != null) {
            val mrzResult = processLines(line1, line2, ocrText)
            if (mrzResult != null) return mrzResult
        }

        val partialResult = tryPartialMrzParse(line1, line2, rawLines, ocrText)
        if (partialResult != null && !partialResult.lastName.isNullOrBlank()) return partialResult

        return parseVisual(rawLines, ocrText)
    }

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

    private fun tryPartialMrzParse(
        line1: String?,
        line2: String?,
        rawLines: List<String>,
        ocrText: String
    ): ParsedPassportData? {
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

    // ── Ligne 1 : noms ────────────────────────────────────────────────────────

    /**
     * FIX principal : détection dynamique de la longueur du préfixe P<CCC
     * au lieu du substring(5) fixe qui cassait sur les passeports DZA mal lus.
     */
    private fun parseNames(line1: String): Pair<String, String> {
        val prefixRegex = Regex("^P<[A-Z]{2,3}")
        val prefixMatch = prefixRegex.find(line1)
        val nameStart   = prefixMatch?.range?.last?.plus(1) ?: 5

        val nameSection = if (nameStart < line1.length) line1.substring(nameStart) else ""
        val parts       = nameSection.split("<<", limit = 2)
        val rawLast     = parts.getOrNull(0)?.replace('<', ' ')?.trim() ?: ""
        val rawFirst    = parts.getOrNull(1)?.replace('<', ' ')?.trim() ?: ""

        return Pair(cleanOcrNoise(rawLast), cleanOcrNoise(rawFirst))
    }

    private fun cleanOcrNoise(name: String): String {
        if (name.length <= 2) return name
        if (name.length > 2 && name[1] == ' ') return name.substring(2).trim()
        val noiseChars = setOf('S', 'B', 'K', 'J')
        if (name[0] in noiseChars && name.length > 4 && name.all { it.isLetter() || it == ' ' }) {
            val candidate = name.substring(1).trim()
            if (candidate.length >= 3) return candidate
        }
        return name
    }

    // ── Ligne 2 ───────────────────────────────────────────────────────────────

    private fun parsePassportNumber(line2: String): String =
        line2.substring(0, 9).trimEnd('<')

    private fun parseNationality(line2: String): String =
        line2.substring(10, 13).trimEnd('<')

    private fun parseDob(line2: String): String =
        formatMrzDate(line2.substring(13, 19))

    private fun parseExpiry(line2: String): String =
        formatMrzDate(line2.substring(20, 26))

    private fun formatMrzDate(yymmdd: String): String {
        return try {
            val yy   = yymmdd.substring(0, 2).toInt()
            val mm   = yymmdd.substring(2, 4)
            val dd   = yymmdd.substring(4, 6)
            val yyyy = if (yy < 30) "20$yy" else "19$yy"
            "$yyyy-$mm-$dd"
        } catch (e: Exception) {
            yymmdd
        }
    }

    // ── Fallback visuel ───────────────────────────────────────────────────────

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
        val passportNumber = findNumberField(
            rawLines,
            listOf("PASSPORT NO", "PASSEPORT N", "DOCUMENT NO", "PASSPO NO"),
            exactOffset = 1
        )

        val vSurname = findField(rawLines, listOf("SURNAME", "NOM", "SURRNAME"), exactOffset = -1)
            ?: findField(rawLines, listOf("SURNAME", "NOM", "SURRNAME"), exactOffset = 1)
            ?: findField(rawLines, listOf("SURNAME", "NOM", "SURRNAME"), exactOffset = 2)

        val vFirstName = findField(rawLines, listOf("GIVEN", "PRÉNOMS", "NAMES"), exactOffset = -1)
            ?: findField(rawLines, listOf("GIVEN", "PRÉNOMS", "NAMES"), exactOffset = 1)
            ?: findField(rawLines, listOf("GIVEN", "PRÉNOMS", "NAMES"), exactOffset = 2)

        var mLastName: String? = null
        var mFirstName: String? = null
        val mrzNameLine = rawLines.find { it.startsWith("P<") }
        if (mrzNameLine != null) {
            val (ml, mf) = parseNames(mrzNameLine)
            mLastName  = ml.ifBlank { null }
            mFirstName = mf.ifBlank { null }
        }

        val finalLastName = listOfNotNull(vSurname?.trim(), mLastName?.trim())
            .filter { it.isNotBlank() }
            .maxByOrNull { it.length } ?: ""

        val finalFirstName = listOfNotNull(vFirstName?.trim(), mFirstName?.trim())
            .filter { it.isNotBlank() }
            .maxByOrNull { it.length } ?: ""

        val dob    = extractDate(ocrText, listOf("BIRTH", "NAISSANCE", "BIRTHF"))
        val expiry = extractDate(ocrText, listOf("EXPIRY", "EXPIRATION"))

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

    private fun findNumberField(lines: List<String>, keywords: List<String>, exactOffset: Int): String? {
        val index = lines.indexOfFirst { line -> keywords.any { kw -> line.contains(kw) } }
        if (index == -1 || index + exactOffset !in lines.indices) return null
        val line = lines[index + exactOffset]
        return line.split(" ", "/", ":").map { it.trim() }.find { it.any { c -> c.isDigit() } }
    }

    private fun extractDate(ocrText: String, keywords: List<String>): String? {
        val lines = ocrText.uppercase().split("\n").map { it.trim() }
        val index = lines.indexOfFirst { line -> keywords.any { kw -> line.contains(kw) } }
        if (index == -1) return null

        val searchArea = (index - 1..index + 1)
            .filter { it in lines.indices }
            .joinToString(" ") { lines[it] }

        val monthPattern = "(JAN|FEV|FEB|MAR|AVR|APR|MAI|MAY|JUIN|JUN|JUIL|JUL|AOUT|AUG|SEP|OCT|NOV|DEC|[0-9]{1,2})"

        val fullDateRegex = Regex("([0-9]{1,2})[^0-9]*$monthPattern[^0-9]*([0-9]{4})")
        fullDateRegex.find(searchArea)?.let { m ->
            return "${m.groupValues[1]} ${m.groupValues[2]} ${m.groupValues[3]}"
        }

        val partialDateRegex = Regex("$monthPattern[^0-9]*([0-9]{4})")
        partialDateRegex.find(searchArea)?.let { m ->
            return "${m.groupValues[1]} ${m.groupValues[2]}"
        }

        val numericDateRegex = Regex("([0-9]{1,2})[/\\-]([0-9]{1,2})[/\\-]([0-9]{4})")
        numericDateRegex.find(searchArea)?.let { m ->
            return "${m.groupValues[1]}/${m.groupValues[2]}/${m.groupValues[3]}"
        }

        return null
    }
}