service ComputeNode
{
	string mapTask(1: string filename),
	string sortTask(1: list<string> filenames)
}
