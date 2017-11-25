from gensim.models.word2vec import Word2Vec

train_filename = 'train.model'
compareA = ''
compareB = ''

# load data
model = Word2Vec.load(train_filename)
# check the similarity between compareA and compareB
sim = model.wv.similarity(compareA, compareB)
