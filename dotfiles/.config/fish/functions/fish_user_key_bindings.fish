function fish_user_key_bindings
	fish_vi_key_bindings
	bind -M insert -m default jk backward-char force-repaint
    for mode in insert default
        bind -M $mode \e\[A history-search-backward
        bind -M $mode -k up history-search-backward
        bind -M $mode \e\[B history-search-forward
        bind -M $mode -k down history-search-forward
    end
    bind -M default j history-search-forward
    bind -M default k history-search-backward
end
