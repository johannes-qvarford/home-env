#!/usr/bin/env node
const fs = require("fs")
const prompt = require('prompt-sync')()
const rimraf = require('rimraf')

if (process.argv.length <= 2) {
    console.log("Usage: " + __filename + " episode-directory");
    process.exit(-1);
}

const show = process.argv[2]
const episodeDirectory = `/mnt/d/data/downloads/completed/${show}`
const newDirectory = `/mnt/d/data/new/${show}`

fs.mkdirSync(newDirectory, { recursive: true })
const filenames = fs.readdirSync(episodeDirectory)

for (const filename of filenames)
{
    fs.linkSync(`${episodeDirectory}/${filename}`, `${newDirectory}/${filename}`)
}

