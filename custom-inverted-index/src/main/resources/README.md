# 倒排索引的 JAVA 实现
最近学习了搜索原理，看了比较多的理论只是，也看了 ES的官网入门文档，但是仍然对 倒排索引，tf/idf，BM25 等之类的概念有些模糊，又不能 一头扎进去 ES
的源码中(目前也不具备这个能力)，就打算手动编写一个倒排索引，用来搜索，当然了，目前还是走的参考，学习，消化、吸收，创新的路子，将别人的知识
转化为自己的知识，给出参考的gitlab https://github.com/bradforj287/SimpleTextSearch ，这个是我找了几个例子并进行对比之后，发现比较适合
练手的一个例子。

下面给出当前事例实现倒排索引的关键元素，倒排索引的入门放弃，我参考的是这篇文章，讲的还行，https://blog.csdn.net/u011239443/article/details/60604017
下面给出关键的消息摘要
Lucene倒排索引原理
Lucerne使用的是倒排文件索引结构。该结构及相应的生成算法如下： 　　

设有两篇文章1和2：

文章1的内容为：Tom lives in Guangzhou,I live in Guangzhou too. 　　

文章2的内容为：He once lived in Shanghai.

倒排索引大概长的样子如下：
关键词            文章号[出现频率]              出现位置 　　
guangzhou           1[2]                      3，6 　　
he                  2[1]                      1 　　
i                   1[1]                      4 　　
live                1[2]                      2，5, 
                    2[1]                      2 　　
shanghai            2[1]                      3 　　
tom                 1[1]                      1

而本项目的实现，并非完全按照上面的倒排索引的样子，而是做了部分冗余；除了倒排索引，还实现了文档的相关性评分，这个比较吊，使用了 tf/idf 算法
来计算相似度，将算法结果作为向量的节点，计算向量的模，并最终基于这些模的值使用 「余弦相似度」 算法来计算目标文档的评分。
然后 ES 使用的是哪个最终算法，这个还得继续查询相关资料，这里的余弦相似度，跟网上找到的普通的预先相似度算法还不一样，估计是一个变种。
接下来给出 本示例 的一些核心类的功能梳理，因为原作者并没有画相关的架构图，可能作者觉得这个东西太 easy 了，不值得画吧。

关键词             TermPosition (word /* term 单词*/，position /*单词出现的未知位置*/)，对应于 guangzhou 3, guangzhou 6
文章&词频          ParsedDocument(doc_id, map<String /*word*/,Integer /*wordCount*/> wordFrequencyMap,List<TermPosition> terms) 
倒排元素           DocumentPosting (ParsedDocument,TermPosition)  表示一篇文章所有的倒排元素   
倒排列表           DocumentPostingCollection (word,List<DocumentPosting>)所有文章的倒排元素的列表
文档相关性指标统计   ParsedDocumentMetrics (Map<String,DocumentPostingCollection> ,ParsedDocument ) double magnitude,Map<String,Double> tfidfCache 
倒排索引           InvertedIndex(Map<Word,PostingList> /*倒排列表*/ ,Map<doc,documentMetrics> /*文档相关性列表*/) ,Search(term) 
搜索结果           SearchResult(score,doc_id)
搜索统计           SearchStat(docNum,nanoSecondsTake)
搜索结果集         SearchResultBatch(SearchStat,List<SearchResult>)