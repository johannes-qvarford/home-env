#!/usr/bin/env node
const { match } = require("assert");
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

    const regex = /(.*E)([0-9]{2}[^0-9].*)/
    const matches = regex.exec(f)
    if (matches != null) {
        //console.log(matches[1], matches[2])

        const finalFilename = `${matches[1]}0${matches[2]}`
        //console.log(finalFilename)
        console.log(finalFilename)
        //fs.renameSync(`${rootDirectory}/${f}`, `${rootDirectory}/${finalFilename}`)
    }
}