function dos2unix
	awk 'BEGIN { RS = "\r\n" } { print }'
end
