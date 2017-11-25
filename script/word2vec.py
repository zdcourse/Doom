from gensim.models.word2vec import Word2Vec
import gensim

train_filename = 'news_12g_baidubaike_20g_novel_90g_embedding_64.bin'
model = gensim.models.KeyedVectors.load_word2vec_format(train_filename, binary=True)
word_filename = '20words'

word2vec = {}
words = []

with open(word_filename, 'r') as p:
    for line in p:
        line_words = line.split()
        words.extend(line_words)

print(len(words))
words = list(set(words))
for source in words:
    print("======{}======".format(source))
    try:
        model[source]
    except:
        del words[words.index(source)]
        continue

    for target in words[words.index(source) + 1:]:
        print(target)
        try:
            word2vec[(source, target)] = model.wv.similarity(source, target)
        except:
            del words[words.index(target)]
            print('word {} not in vocabulary'.format(target))

print('*' * 10)

with open('word_file', 'a') as f:
    f.write(' '.join(words))
f.close()

for (s, t), v in word2vec.items():
    with open('word_vec_file', 'a') as fp:
        fp.write("{} {} {}".format(s, t, v) + '\n')

fp.close()
