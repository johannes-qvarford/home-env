#!/usr/bin/env node

const scan = require('./util/scan')

// Johnny Bravo - 1x01a - Johnny Bravo.avi

scan.seasonRename({
    executableFilename: __filename,
    replacement: "Johnny Bravo S__season__E__episode__.avi",
    episodeExtractor: scan.regexEpisodeExtractor(/^Johnny Bravo.*.avi$/),
    episodeDigits: 2,
    seasonDigits: 2,
    useAlphabeticalEpisodeNumber: true
})