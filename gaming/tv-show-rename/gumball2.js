#!/usr/bin/env node

const scan = require('./util/scan')

// The Amazing World of Gumball - 101, 102 - The Responsible - The DVD.mkv

scan.seasonRename({
    executableFilename: __filename,
    replacement: "The Amazing World of Gumball S__season__E__episode__-S__season__E__episode2__.mkv",
    episodeExtractor: scan.regexEpisodeExtractor(/^The Amazing World of Gumball - [0-9](?<episode>[0-9]{2}), [0-9](?<episode2>[0-9]{2})[^0-9]+\.mkv$/),
    episodeDigits: 2,
    seasonDigits: 2
})