#!/usr/bin/env node
const fs = require("fs");
const { exit } = require("process");

// '/mnt/g/TemporaryMedia/Videos/TV Shows/Animaniacs/Season 01'

// Turn
// Animaniacs - S01E001-E003 - De-Zanitized - The Monkey Song - Nighty-Night Toon.mkv
// Into
// Animaniacs 1x001-1x002-1x003.mkv

if (process.argv.length <= 2) {
    console.log("Usage: " + __filename + " path/to/directory");
    process.exit(-1);
}

const rootDirectory = process.argv[2]
const filenames = fs.readdirSync(rootDirectory)

for (const f of filenames)
{
    //console.log(`Now handling ${f}`)
    function leftpadNumber(n) {
        return n < 10 ? "00"+n :
            n < 100 ? "0"+n : ""+n;
    }

    function range(start, end) {
        var ans = [];
        for (let i = start; i <= end; i++) {
            ans.push(i);
        }
        return ans;
    }

    const multiRegex = /.*S0(.)E([0-9]{2,3})-E([0-9]{2,3}).*\.(.*)/
    const singleRegex = /.*S0(.)E([0-9]{2,3}).*\.(.*)/

    const isMultiFile = () => {
        return multiRegex.test(f)
    }

    const extractFromMultiFile = () => {
        const [_, season, start, end, extension] = multiRegex.exec(f)
        return {
            season,
            start,
            end,
            extension
        }
    }

    const extractFromSingleFile = () => {
        const [_, season, start, extension] = singleRegex.exec(f)
        return {
            season,
            start,
            end: start,
            extension
        }
    }

    const { season, start, end, extension } = isMultiFile() ? extractFromMultiFile() : extractFromSingleFile()
    
    const episodeNumbers = start === end ? [start] : range(parseInt(start), parseInt(end))
    const paddedNumbers = episodeNumbers.map(leftpadNumber)
    const episodeSegment = paddedNumbers.map(s => `s0${season}e${s}`).join("-")
    const finalFilename = `Animaniacs ${episodeSegment}.${extension}`;

    if (!isMultiFile()) {
        //console.log(f, episodeNumbers, paddedNumbers, episodeSegment, finalFilename)
    }

    console.log(`${rootDirectory}/${f}`, `${rootDirectory}/${finalFilename}`)
    //fs.renameSync(`${rootDirectory}/${f}`, `${rootDirectory}/${finalFilename}`)
}