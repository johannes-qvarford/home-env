#!/usr/bin/env node

const scan = require('./util/scan')

// The Amazing World of Gumball - 109 - The Pressure.mkv

scan.seasonRename({
    executableFilename: __filename,
    replacement: "The Amazing World of Gumball S__season__E__episode__.mkv",
    episodeExtractor: scan.regexEpisodeExtractor(/^The Amazing World of Gumball - [0-9](?<episode>[0-9]{2})[^0-9]+\.mkv$/),
    episodeDigits: 2,
    seasonDigits: 2
})