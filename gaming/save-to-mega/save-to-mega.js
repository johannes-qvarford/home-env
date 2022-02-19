
console.log("Hello!")
try {
    console.log(browser)
    browser.contextMenus.create({
        id: "save-to-mega",
        title: "Save to Mega",
        contexts: ["image", "audio", "link", "video"]
      }, () => console.log("Menu item created!"));
    browser.contextMenus.onClicked.addListener(e => console.log("you clicked!", e));
    console.log("Hello again!");
} catch (error) {
    console.log(error)
}
