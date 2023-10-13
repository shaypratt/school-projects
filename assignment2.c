#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>
#include <string.h>
#include <sys/wait.h>
#include <signal.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <sys/stat.h>

int summation(int start, int end)
{
	int sum = 0;
	if (start < end)
	{
		sum = ((end * (end + 1)) - (start * (start - 1))) / 2;
	}
	return sum;
}

int ith_part_start(int i, int N, int M)
{
	int part_size = N / M;
	int start = i * part_size;
	return start;
}
int ith_part_end(int i, int N, int M)
{
	int part_size = N / M;
	int end = (i < M - 1) ? ((i + 1) * part_size - 1) : N;
	return end;
}
int main(int argc, char **argv){
    printf("parent(PID %d): process started\n", getpid());
    int m = atoi(argv[2]);
    int n = atoi(argv[1]);
    int port[2];
    if(pipe(port)<0){
        perror("pipe error");
        exit(0);
    }
    printf("parent(PID %d): forking child_1\n", getpid());
	pid_t pid;
    pid=fork();
    if(pid<0){
        perror("fork error");
        exit(0);
    }
    else if(pid==0){  // child_1 process
        printf("parent(PID %d): fork successful for child_1(PID %d)\n", getppid(), getpid());
        printf("parent(PID %d): waiting for child_1(PID %d) to complete\n\n", getppid(), getpid());
        printf("child_1(PID %d): process started from parent(PID %d)\n", getpid(), getppid());
        printf("child_1(PID %d): forking child_1.1....child_1.%d\n\n", getpid(), m);
        for (int i=0; i<m; i++){
            pid_t pid2;
            pid2=fork();
            if(pid2<0){
                perror("fork error");
                exit(0);
            }
            if(pid2==0){ // child_1.i process
                printf("child_1.%d(PID %d): fork successful\n", i+1, getpid());
                int end = ith_part_end(i,n,m);
                int start = ith_part_start(i,n,m);
                int sum = summation(start,end);
                printf("child_1.%d(PID %d): partial sum: [%d - %d] = %d\n",i+1, getpid(), start, end, sum);
                write(port[1], &sum, sizeof(sum));
                _exit(0);
            }
            wait(NULL);
        }
        int num;
        int total = 0;
        for(int j=0; j<m; j++){
            read(port[0],&num,sizeof(int));
            total+=num;
        }
        printf("\nchild_1(PID %d): total sum = %d\n", getpid(), total);
        printf("child_1(PID %d): child_1 completed\n\n", getpid());
    }
    else{  // parent process
        wait(NULL);
        printf("parent(PID %d): parent completed\n", getpid());
    }
}