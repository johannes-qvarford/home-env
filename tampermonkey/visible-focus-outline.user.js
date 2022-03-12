// ==UserScript==
// @name         visible-focus-outline
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  try to take over the world!
// @author       You
// @match        https://*/*
// @icon         data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==
// @grant        none
// ==/UserScript==

(function() {
    'use strict';
    var rule = `
    *:focus {
      outline: red !important;
      outline-style: solid !important;
      outline-width: 3px !important;
    }
    `;
    var styleSheet = document.createElement("style")
    styleSheet.type = "text/css"
    styleSheet.innerText = rule
    document.head.appendChild(styleSheet)
    // Your code here...
})();