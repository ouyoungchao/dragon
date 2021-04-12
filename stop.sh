ps -ef|grep dragon|awk '{print $2}'|xargs kill -9
