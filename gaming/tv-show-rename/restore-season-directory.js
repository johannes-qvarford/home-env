#!/usr/bin/env node
const fs = require("fs")
const prompt = require('prompt-sync')()
const rimraf = require('rimraf')

if (process.argv.length <= 3) {
    console.log("Usage: " + __filename + " show/season-directory backup-id");
    process.exit(-1);
}

const show = process.argv[2]
const rootDirectory = `/mnt/d/data/media/tv/${show}/`
const backupId = process.argv[3]
const backupDirectory = `/mnt/d/BackupRenamedVideos/${backupId}`
const filenames = fs.readdirSync(backupDirectory)

rimraf.sync(rootDirectory)
fs.mkdirSync(rootDirectory)

for (const filename of filenames)
{
    fs.linkSync(`${backupDirectory}/${filename}`, `${rootDirectory}/${filename}`)
}

