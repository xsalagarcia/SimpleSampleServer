Just a simple sample server.

Server class is a Runnable that does a loop with ServerSocket.accept() and adds the gotten socket to a LinkedBlockingQueue.

When server runs, creates some WorkerThread. The created workedTrheads do a loop with linkedBlockingQueue.take() waiting for sockets to deal.

Starter just starts the server.
