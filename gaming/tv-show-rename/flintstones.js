#!/usr/bin/env node

const scan = require('./util/scan')

// mtn-flint501.avi

scan.seasonRename({
    executableFilename: __filename,
    replacements: [
        "The Flintstones S__season__E__episode__.avi",
    ],
    episodeExtractor: scan.regexEpisodeExtractor(/^.*[0-9](?<episode>[0-9]{2}).*$/),
    episodeDigits: 2,
    seasonDigits: 2,
})