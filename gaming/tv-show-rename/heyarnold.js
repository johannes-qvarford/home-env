#!/usr/bin/env node

const scan = require('./util/scan')

// hey arnold! - 1x01 - downtown as fruits ~ eugene's bike.avi

scan.seasonRename({
    executableFilename: __filename,
    replacements: [
        "Hey Arnold! S__season__E__episode__.avi",
        "Hey Arnold! S__season__E__episode__-S__season__E__episode2__.avi",
    ],
    episodeExtractor: scan.regexEpisodeExtractor(/^.*.avi$/),
    episodeDigits: 2,
    seasonDigits: 2,
    useAlphabeticalEpisodeNumber: true,
    episodeCountExtractor: filename =>  ((filename.match(/~/g) || []).length) + 1
})