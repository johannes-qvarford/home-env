function all_sql_strings
	cat OptimoveExportLogic.java | grep \" | egrep '^[[:space:]]*(\+|String sql)' | gsed 's/String sql = "/\n/' | sed 's/"$//' | gsed 's/+" //'
end
