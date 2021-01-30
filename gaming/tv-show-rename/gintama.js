#!/usr/bin/env node

const scan = require('./util/scan')

scan.seasonRename({
    executableFilename: __filename,
    replacement: "Gintama S__season__E__episode__.mkv",
    episodeExtractor: scan.regexEpisodeExtractor(/Gintama Ep (?<episode>[0-9]{2}).mkv/),
    episodeDigits: 2,
    seasonDigits: 2
})