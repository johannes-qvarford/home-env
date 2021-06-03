(require 'package)
(add-to-list 'package-archives
	      '("melpa" . "https://melpa.org/packages/")
	      t)
(custom-set-variables
 ;; custom-set-variables was added by Custom.
 ;; If you edit it by hand, you could mess it up, so be careful.
 ;; Your init file should contain only one such instance.
 ;; If there is more than one, they won't work right.
 '(package-selected-packages '(racket-mode smartparens magit)))
(custom-set-faces
 ;; custom-set-faces was added by Custom.
 ;; If you edit it by hand, you could mess it up, so be careful.
 ;; Your init file should contain only one such instance.
 ;; If there is more than one, they won't work right.
 )
(require 'smartparens-config)
(add-hook 'racket-mode-hook #'smartparens-mode)
(add-hook 'racket-mode-hook #'racket-xp-mode)
(add-hook 'emacs-lisp-mode-hook       #'smartparens-mode)
(add-hook 'eval-expression-minibuffer-setup-hook #'smartparens-mode)
(add-hook 'ielm-mode-hook             #'smartparens-mode)
(add-hook 'lisp-mode-hook             #'smartparens-mode)
(add-hook 'lisp-interaction-mode-hook #'smartparens-mode)
(add-hook 'scheme-mode-hook           #'smartparens-mode)