#!/bin/bash
# awk 是一个强大的文本分析工具 , 相对于 grep 的查找，sed 的编辑，awk 在数据分析和生成报告时，显得尤为强大
# 简单来说 awk 就是把文件逐行读入，（空格，制表符）为默认的分隔符，将每行切片（分列），切开的部分再进行各种处理
# 语法格式为，awk -F {pattern + acton} {filename}. 支持自定义分隔符，支持正则表达式匹配，
  #支持自定义变量，数组 a[1],a[atom], map(key)
  # 支持内置变量 ARGC-命令行参数个数，ARGV-命令行参数排列，ENVIRON 支持队列中环境变量的使用
  # NF 浏览记录的域的个数，就是当前的列数
  # NR 已读的行数
# 支持函数，print，split，substr，sub，gsub
# 支持流程控制语句，类 C 语言，if，while，do/while，for，break，continue
# 以 passwd 为例，一共 7 列，分别代表：用户名称：密码：宿主：宿组 (id root ：uid=0(root) gid=0(root) groups=0(root))：用户的描述：当前用户的根目录：
 awk -F ":" '{print $1}' /etc/passwd cut 同样的效果代码为 cut -d':' -f1 /etc/passwd
 # 只是显示 /etc/passwd 的帐户以及账户对应的 shell ，而账户与 shell 之间用制表符分隔，而且在所有的行开始前添加列名name，shell，且在最后一行添加blue，/bin/nosh
 awk -F ':' 'BEGIN{print "name\tshell"}{print $1 "\t" $7}END{print "blue\t/bin/nosh"}' /etc/passwd
 # 搜索 /etc/passwd 中的有 root 关键字的所有行
 awk '/root/ {print $0}' /etc/passwd
 #统计 /etc/passwd 中每行的行号，每行的列数，对应的完整内容
 awk -F ':' '{print NR "\t" NF "\t" $0}' /etc/passwd


