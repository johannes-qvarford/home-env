#!/usr/bin/env node
const fs = require("fs")

// '/mnt/g/TemporaryMedia/Videos/Backup/Season 01'
// '/mnt/g/TemporaryMedia/Videos/TV Shows/Animaniacs/Season 01'



if (process.argv.length <= 3) {
    console.log("Usage: " + __filename + "original new-directory");
    process.exit(-1);
}

const [_, __, originalDirectory, newDirectory] = process.argv
const filenames = fs.readdirSync(originalDirectory)

for (const f of filenames)
{
    console.log(`${originalDirectory}/${f}`, `${newDirectory}/${f}`)
    fs.linkSync(`${originalDirectory}/${f}`, `${newDirectory}/${f}`)
}