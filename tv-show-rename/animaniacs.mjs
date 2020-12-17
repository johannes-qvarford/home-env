#!/usr/bin/env node
const fs = require("fs")

// '/mnt/g/TemporaryMedia/Videos/TV Shows/Animaniacs (1993)/Animaniacs Season 2 (1994)'

if (process.argv.length <= 3) {
    console.log("Usage: " + __filename + " path/to/directory season");
    process.exit(-1);
}

const rootDirectory = process.argv[2]

const filenames = fs.readdirSync(rootDirectory)
const season = process.argv[3]
let episodeNumber = 1;
for (const f of filenames)
{
    function leftpadNumber(n) {
        return n < 10 ? "0"+n : n;
    }

    const regex = /[0-9]{1,2} - (.*).mp4/
    const subEpisodes = regex.exec(f)
    if (subEpisodes == null) {
        continue;
    }
    const splitSubEpisodes = subEpisodes[1].replace("  ", " - ").split(" - ")
    startingEpisodeString = leftpadNumber(episodeNumber)
    endingEpisodeNumber = episodeNumber + (splitSubEpisodes.length - 1)
    endingEpisodeString = splitSubEpisodes.length === 1 ? "" : "-E" + leftpadNumber(endingEpisodeNumber)
    episodeNumber+=splitSubEpisodes.length;
    const finalFilename = `Animaniacs - S0${season}E${startingEpisodeString}${endingEpisodeString} - ${subEpisodes[1]}.mp4`
    //console.log(finalFilename)
    console.log(finalFilename)
    //fs.renameSync(`${rootDirectory}/${f}`, `${rootDirectory}/${finalFilename}`)
    //console.log(`${rootDirectory}/${f}`, `${rootDirectory}/${finalFilename}`)
}