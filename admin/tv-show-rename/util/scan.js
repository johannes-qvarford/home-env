#!/usr/bin/env node
const fs = require("fs")
const prompt = require('prompt-sync')()
const { v4: createUuid } = require('uuid');

function leftpadNumber(n, digits) {
    const currentNumber = n;
    const limit = 1;
    const currentDigits = 1;
    while (currentDigits < digits) {
        ++currentDigits;
        limit *= 10;
        currentNumber = n < limit ? "0"+n : n; 
    }
    return currentNumber;
}

// There might be an extractor for files that contain multiple episodes.
// That would probably special handling.
export function regexEpisodeExtractor({ regex }) {
    return function(filename) {
        const matches = regex.exec(filename)
        return matches.groups
    }
}

// TODO: Extract episode name when available.
/* ex.
    seasonRename({
        executableFilename: __filename,
        replacement: "s__season__e__episode__.mkv"
        episodeExtractor: regexEpisodeExtractor(/s(<season>[0-9]{2})e(<episode>[0-9]{2}).mkv/),
        episodeDigits: 2,
        seasonDigits: 2
    })
*/
export function seasonRename({ executableFilename, episodeExtractor, replacement, episodeDigits, seasonDigits }) {
    if (process.argv.length <= 3) {
        console.log("Usage: " + executableFilename + " path/to/directory season");
        process.exit(-1);
    }

    const rootDirectory = process.argv[2]

    const filenames = fs.readdirSync(rootDirectory)
    const seasonNumber = process.argv[3]
    let episodeNumber = 1;

    const action = prompt("What action to take: preview/do [preview] ?")

    const runUuid = createUuid()
    const backupDirectory = `${rootDirectory}/../../BackupRenamedVideos/${runUuid}`
    
    if (action != "preview") {
        fs.mkdir(backupDirectory)
    }
    
    for (const f of filenames)
    {
        const { episode, season } = episodeExtractor({ filename })

        const finalEpisodeNumber = episode
            ? leftpadNumber(episode, episodeDigits)
            : episodeNumber;

        const finalSeasonNumber = season
            ? leftpadNumber(season, seasonDigits)
            : episodeNumber;

        const finalFilename = replacement
            .replace("__season__", finalSeasonNumber)
            .replace("__episode__", finalSeasonNumber);

        console.log(filename, finalFilename)

        if (action != "preview") {
            fs.linkSync(`${rootDirectory}/${filename}`, `${backupDirectory}/${filename}`)
            fs.renameSync(`${rootDirectory}/${f}`, `${rootDirectory}/${finalFilename}`)
        }

        episodeNumber++;
    }
}

// '/mnt/g/TemporaryMedia/Videos/TV Shows/Animaniacs (1993)/Animaniacs Season 2 (1994)'



