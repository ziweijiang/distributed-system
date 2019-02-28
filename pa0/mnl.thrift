struct Pair {
1: string key,
2: string value = ""
}
service Manipulate {
bool ping(),
string get(1: string k),
bool put(1: Pair p)
}