struct Result {
1: list<string> order,
2: i32 time
}
service MapReduce {
Result cal(1: string address),
bool compute(1: string address)
}