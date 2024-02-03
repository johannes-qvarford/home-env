def "gitx ent stage" [] {
    git status -s |
        lines |
        each {|x| $x | split column ' ' --collapse-empty status fpath } |
        flatten |
        each {|x| { ...$x ppath: $x.fpath } };
}
# git show <id>