#!/usr/bin/env node

const scan = require('./util/scan')

scan.seasonRename({
    executableFilename: __filename,
    replacement: "Bob's Burgers (2011) S__season__E__episode__.mp4",
    episodeExtractor: scan.regexEpisodeExtractor(/.* Episode (?<episode>[0-9]{2}).mp4/),
    episodeDigits: 2,
    seasonDigits: 2
})