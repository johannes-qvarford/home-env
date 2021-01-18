#!/usr/bin/env node

const scan = require('./util/scan')

scan.seasonRename({
    executableFilename: __filename,
    replacement: "Fullmetal Alchemist Brotherhood (2009) S__season__E__episode__.mkv",
    episodeExtractor: scan.regexEpisodeExtractor(/^.*Brotherhood E(?<episode>[0-9]{2}).*$/),
    episodeDigits: 2,
    seasonDigits: 2
})