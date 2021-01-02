function unix2dos
	awk 'BEGIN { ORS = "\r\n" } { print }'
end
