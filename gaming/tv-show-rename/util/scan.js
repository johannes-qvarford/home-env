#!/usr/bin/env node
const fs = require("fs")
const prompt = require('prompt-sync')()
const { v4: createUuid } = require('uuid');

function leftpadNumber(n, digits) {
    let currentNumber = n;
    let limit = 1;
    let currentDigits = 1;
    while (currentDigits < digits) {
        ++currentDigits;
        limit *= 10;
        currentNumber = currentNumber < limit ? "0"+currentNumber : currentNumber; 
    }
    return currentNumber;
}

// There might be an extractor for files that contain multiple episodes.
// That would probably require special handling.
function regexEpisodeExtractor(regex) {
    return function(filename) {
        const matches = regex.exec(filename)
        if (!matches) {
            return {}
        }
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
function seasonRename({ executableFilename, episodeExtractor, replacement, episodeDigits, seasonDigits }) {
    if (process.argv.length <= 3) {
        console.log("Usage: " + executableFilename + " show/season-directory season");
        process.exit(-1);
    }

    const show = process.argv[2]
    const rootDirectory = `/mnt/d/data/media/tv/${show}/`

    const filenames = fs.readdirSync(rootDirectory)
    const seasonNumber = process.argv[3]
    let episodeNumber = 1;

    const action = prompt("What action to take: preview/do [preview] ?")

    const runUuid = createUuid()
    const backupDirectory = `/mnt/d/BackupRenamedVideos/${runUuid}`
    
    if (action == "do") {
        fs.mkdirSync(backupDirectory)
    }
    
    for (const filename of filenames)
    {
        const { episode, episode2, season } = episodeExtractor(filename)

        if (episode == null) {
            continue;
        }

        const finalEpisodeNumber =
            leftpadNumber(
                episode != null
                    ? parseInt(episode)
                    : episodeNumber,
                episodeDigits);
        
        const finalEpisodeNumber2 =
            leftpadNumber(
                episode != null
                    ? parseInt(episode2)
                    : null,
                episodeDigits);


        const finalSeasonNumber =
            leftpadNumber(
                season != null
                    ? parseInt(season)
                    : seasonNumber,
                seasonDigits);

        const finalFilename = replacement
            .replace(/__season__/g, finalSeasonNumber)
            .replace("__episode__", finalEpisodeNumber)
            .replace("__episode2__", finalEpisodeNumber2);

        console.log(filename, "\n", finalFilename)

        if (action == "do") {
            fs.linkSync(`${rootDirectory}/${filename}`, `${backupDirectory}/${filename}`)
            fs.renameSync(`${rootDirectory}/${filename}`, `${rootDirectory}/${finalFilename}`)
        }

        episodeNumber++;
    }
}

module.exports = {
    regexEpisodeExtractor,
    seasonRename
}

// '/mnt/g/TemporaryMedia/Videos/TV Shows/Animaniacs (1993)/Animaniacs Season 2 (1994)'



