struct ServerData
{
    //mode: 1: Random, 2: LoadBalancing
    1: i32 mode,
    2: i32 nodeID;
}

service MasterServer
{
    string job(1: list<string> filenames),

    ServerData registerNode(1: string ip, 2: i32 port),
    void noticeFinishedMap(1: string resultFilename),
    void noticeFinishedSort(1: string resultFilename)
}
