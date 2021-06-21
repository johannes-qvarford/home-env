(require 'package)
(add-to-list 'package-archives '("melpa" . "https://melpa.org/packages/") t)
(custom-set-variables
 ;; custom-set-variables was added by Custom.
 ;; If you edit it by hand, you could mess it up, so be careful.
 ;; Your init file should contain only one such instance.
 ;; If there is more than one, they won't work right.
 '(package-selected-packages
   '(smartparens evil-surround evil-collection evil dracula-theme rainbow-delimiters racket-mode magit)))
(custom-set-faces
 ;; custom-set-faces was added by Custom.
 ;; If you edit it by hand, you could mess it up, so be careful.
 ;; Your init file should contain only one such instance.
 ;; If there is more than one, they won't work right.
 )

(setq evil-want-keybinding nil)

(require 'rainbow-delimiters)
(require 'racket-xp)
(require 'evil)
(require 'evil-surround)
(require 'smartparens)

;; CUSTOM

(evil-mode 1)
(evil-collection-init)
(setq visible-bell 1)
(global-evil-surround-mode 1)
(smartparens-global-mode 1)

;; KEYBINDINGS

(evil-define-key '(normal visual) 'global
  "s" nil
  "sw" 'save-buffer
  "sf" 'find-file
  "sq" 'save-buffers-kill-terminal)

(evil-define-key 'insert 'global
  "jk" 'evil-force-normal-state
  "jj" 'self-insert-command
  "js" (lambda () (interactive)
	 (insert "js")))

;; THEME

(add-to-list 'custom-theme-load-path "~/.emacs.d/themes")
(load-theme 'dracula t)

;; RACKET

(add-hook 'racket-mode-hook #'racket-xp-mode)
(add-hook 'racket-mode-hook #'rainbow-delimiters-mode)

(sp-with-modes 'racket-mode
  (sp-local-pair "'" nil :actions nil))

;; LISPS

(add-hook 'emacs-lisp-mode-hook #'rainbow-delimiters-mode)

(add-hook 'eval-expression-minibuffer-setup-hook #'smartparens-mode)
(add-hook 'eval-expression-minibuffer-setuo-hook #'rainbow-delimiters-mode)

(add-hook 'ielm-mode-hook #'rainbow-delimiters-mode)

(add-hook 'lisp-mode-hook #'rainbow-delimiters-mode)

(add-hook 'lisp-interaction-mode-hook #'rainbow-delimiters-mode)

(add-hook 'scheme-mode-hook #'rainblow-delimiters-mode)
