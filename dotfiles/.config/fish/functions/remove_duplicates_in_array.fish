function remove_duplicates_in_array
    if test (count $argv) = 1
        set -l newvar
        set -l count 0
        for v in $$argv
            if contains -- $v $newvar
                inc count
            else
                set newvar $newvar $v
            end
        end
        set $argv $newvar
    else
        for a in $argv
            varclear $a
        end
    end
end
