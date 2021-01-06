const { program } = require('commander');
const fetch = require('node-fetch');
const { v4: createUuid } = require('uuid');
const prompt = require('prompt-sync')()
const { execSync } = require('child_process')
const { unlinkSync, writeFileSync } = require('fs')
const arrayBufferToBuffer = require('arraybuffer-to-buffer');


program.version('0.0.1');

// Currently only supports twitter-media
program.option('-x, --extractor <type>', 'how is the content extracted from the link', 'twitter-media')
program.option('-u, --url <tostore>', 'url whose content should be stored')

program.parse(process.argv);

const getContentArrayBufferFromUrl = async (url) => {
    const response = await fetch(url)
    const status = await response.status;
    if (status > 299) {
        const errorResponseBody = await response.text()
        throw Error(`${status} ${errorResponseBody}`)
    }
    return await response.arrayBuffer()
}

class Mega
{
    constructor()
    {
        this.username = null;
        this.passsword = null;
    }

    uploadFileAtPath(path) {
        this.ensureLoggedIn()
        const output = execSync(`megatools put --path /Root/Private/New '${path}' --username '${this.username}' --password '${this.password}'`)
    }

    ensureLoggedIn(path) {
        if (this.username == null || this.password == null) {
            this.username = prompt("Meganz email: ")
            this.password = prompt("Meganz password: ", null, { echo: '*' })
        }
    }
}

const determineFilenameFromUrl = (url) => {
    // For now, just expect that the url has a file extension
    // example url: https://pbs.twimg.com/tweet_video_thumb/EoaKIDUXcAILcW0.jpg
    // The basename is random at the moment.
    const extension = /.*\.([^.]*)/.exec(url)[1]
    const basename = createUuid()
    return `${basename}.${extension}`
}

const extractTweetIdFromTweetUrl = (url) => {
    // Example tweet_ "https://twitter.com/xkcd/status/1346097439284060162"
    // The below regex also removes potential query parameters.
    const id = RegExp(".*/([^/?]*)").exec(url)[1]
    return id;
}

const twitterMediaExtractor = async (url) => {
    const id = extractTweetIdFromTweetUrl(url)
    const result = await fetch(`https://api.twitter.com/1.1/statuses/show/${id}.json`, {
        headers: {
            "Authorization": "Bearer TODO"
        }
    })
    const status = await result.status
    if (status > 299) {
        console.log(status)
        console.log(await result.text())
        return;
    }

    const response = await result.json()

    const media = response.entities.media.map(e => e)
    const extendedMedia = response.extended_entities.media.map(e => e)
    const allMediaUrls = media.concat(extendedMedia).map(u => u.media_url_https);
    const allMediaUrlsWithoutDuplicates = [...new Set(allMediaUrls)];
    
    const allFilePromises = allMediaUrlsWithoutDuplicates.map(async u => ({
        filename: determineFilenameFromUrl(u),
        content: await getContentArrayBufferFromUrl(u)
    }))
    const allFiles = await Promise.all(allFilePromises);

    return allFiles
}

const extractors = {
    'twitter-media': twitterMediaExtractor
}

const pathFromFile = (file) => {
    return `/tmp/${file.filename}`;
}

const serializeFile = (file) => {
    writeFileSync(pathFromFile(file), arrayBufferToBuffer(file.content))
}

const removeFile = (file) => {
    unlinkSync(pathFromFile(file))
}

async function main () {
    const mega = new Mega()
    const files = await extractors[program.extractor](program.url)

    files.forEach(f => {
        serializeFile(f)
        mega.uploadFileAtPath(pathFromFile(f))
        removeFile(f)
    })
    
}

main()