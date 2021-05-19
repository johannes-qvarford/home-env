#lang rash

(require racket/string)
(require "util.rkt")

;def f 10
(values f)

exit
; TODO: write-trimmed-string maybe?
; Or use def from rash demo like this?
; def temp mktemp -d |>> string-trim
(define temp-port (open-output-string))
mktemp -d |>> string-trim |>> write-string _ temp-port
(define temp (get-output-string temp-port))

sudo dnf install -y make clang gmp-devel
curl -sSL https://get.haskellstack.org/ | sh
git clone https://github.com/Shuumatsu/racket-pretty-printer "$temp"
cd "$temp/core"
stack install