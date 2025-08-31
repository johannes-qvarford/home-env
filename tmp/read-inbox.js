const { chromium } = require('playwright');

async function scrapeProtonMailEmails() {
    // Launch browser with MS Edge (since that's what worked for you)
    const browser = await chromium.launch({ 
        headless: false, // Set to true for headless mode
        channel: 'msedge' // Use MS Edge as configured
    });
    
    const context = await browser.newContext();
    const page = await context.newPage();
    
    try {
        console.log('Navigating to ProtonMail...');
        await page.goto('https://mail.proton.me');
        
        // Wait for login page to load
        await page.waitForSelector('input[type="email"], input[type="text"]', { timeout: 10000 });
        console.log('Login page loaded. Please sign in manually...');
        
        // Wait for user to manually sign in
        // You could add automated login here if you want, but it's safer to do manually
        console.log('Waiting for you to sign in...');
        
        // Wait for the inbox to load (look for the main navigation or inbox heading)
        await page.waitForSelector('heading:has-text("Inbox")', { timeout: 60000 });
        console.log('Successfully logged in and reached inbox!');
        
        // Check if we're already in the inbox, if not navigate there
        const currentUrl = page.url();
        if (!currentUrl.includes('/inbox')) {
            console.log('Navigating to inbox...');
            await page.click('link:has-text("Inbox")');
            await page.waitForSelector('heading:has-text("Inbox")');
        }
        
        // Wait a moment for emails to load
        await page.waitForTimeout(2000);
        
        // Check if there are emails or if inbox is empty
        const noMessagesElement = await page.$('text=No messages found');
        
        if (noMessagesElement) {
            console.log('📭 Inbox is empty - no messages found');
            return [];
        }
        
        // Extract email titles/subjects
        console.log('📧 Extracting email titles...');
        
        // Look for email regions/items (based on the structure we saw)
        const emailElements = await page.$$('region[aria-label], [role="region"]');
        
        const emailTitles = [];
        
        for (let i = 0; i < emailElements.length; i++) {
            try {
                // Try to get the heading within each email region
                const headingElement = await emailElements[i].$('h2, h3, [role="heading"]');
                
                if (headingElement) {
                    const titleText = await headingElement.textContent();
                    if (titleText && titleText.trim() !== '') {
                        emailTitles.push(titleText.trim());
                    }
                }
            } catch (error) {
                // Skip this element if we can't extract the title
                continue;
            }
        }
        
        // Alternative method: look for specific email title patterns
        if (emailTitles.length === 0) {
            console.log('Trying alternative email title extraction...');
            
            // Look for elements that contain email subjects based on the structure we observed
            const subjectElements = await page.$$('[data-testid*="subject"], .subject, [aria-label*="subject"]');
            
            for (const element of subjectElements) {
                try {
                    const text = await element.textContent();
                    if (text && text.trim() !== '') {
                        emailTitles.push(text.trim());
                    }
                } catch (error) {
                    continue;
                }
            }
        }
        
        // Print results
        if (emailTitles.length > 0) {
            console.log(`\n📬 Found ${emailTitles.length} emails:`);
            console.log('=' .repeat(50));
            
            emailTitles.forEach((title, index) => {
                console.log(`${index + 1}. ${title}`);
            });
        } else {
            console.log('❌ Could not extract email titles. The page structure might have changed.');
            console.log('Current page URL:', page.url());
            
            // Take a screenshot for debugging
            await page.screenshot({ path: 'debug_screenshot.png' });
            console.log('📸 Debug screenshot saved as debug_screenshot.png');
        }
        
        return emailTitles;
        
    } catch (error) {
        console.error('❌ Error occurred:', error.message);
        
        // Take a screenshot for debugging
        try {
            await page.screenshot({ path: 'error_screenshot.png' });
            console.log('📸 Error screenshot saved as error_screenshot.png');
        } catch (screenshotError) {
            console.log('Could not take error screenshot');
        }
        
        return [];
    } finally {
        // Uncomment the next line if you want the browser to close automatically
        // await browser.close();
        
        console.log('\n⏸️  Browser left open for manual inspection. Close manually when done.');
    }
}

// Function to scrape emails from a specific folder
async function scrapeEmailsFromFolder(folderName) {
    const browser = await chromium.launch({ 
        headless: false,
        channel: 'msedge'
    });
    
    const context = await browser.newContext();
    const page = await context.newPage();
    
    try {
        console.log(`Navigating to ProtonMail ${folderName} folder...`);
        await page.goto('https://mail.proton.me');
        
        // Wait for login and manual authentication
        await page.waitForSelector('heading:has-text("Inbox")', { timeout: 60000 });
        
        // Navigate to the specific folder
        console.log(`Clicking on ${folderName} folder...`);
        await page.click(`text=Folder ${folderName}`);
        
        // Wait for folder to load
        await page.waitForSelector(`heading:has-text("${folderName}")`, { timeout: 10000 });
        
        // Extract emails from the folder (same logic as above)
        await page.waitForTimeout(2000);
        
        const emailElements = await page.$$('region[aria-label], [role="region"]');
        const emailTitles = [];
        
        for (let i = 0; i < emailElements.length; i++) {
            try {
                const headingElement = await emailElements[i].$('h2, h3, [role="heading"]');
                if (headingElement) {
                    const titleText = await headingElement.textContent();
                    if (titleText && titleText.trim() !== '') {
                        emailTitles.push(titleText.trim());
                    }
                }
            } catch (error) {
                continue;
            }
        }
        
        console.log(`\n📁 Found ${emailTitles.length} emails in ${folderName}:`);
        emailTitles.forEach((title, index) => {
            console.log(`${index + 1}. ${title}`);
        });
        
        return emailTitles;
        
    } catch (error) {
        console.error('❌ Error occurred:', error.message);
        return [];
    } finally {
        console.log('\n⏸️  Browser left open for manual inspection.');
    }
}

// Main execution
async function main() {
    console.log('🚀 Starting ProtonMail Email Scraper...');
    console.log('Make sure you have MS Edge installed and configured for Playwright');
    
    // Scrape inbox
    const inboxEmails = await scrapeProtonMailEmails();
    
    // Uncomment to also scrape a specific folder (e.g., Nexer)
    // const nexerEmails = await scrapeEmailsFromFolder('Nexer');
    
    console.log('\n✅ Scraping completed!');
}

// Run the script
if (require.main === module) {
    main().catch(console.error);
}

// Export functions for use in other scripts
module.exports = {
    scrapeProtonMailEmails,
    scrapeEmailsFromFolder
};