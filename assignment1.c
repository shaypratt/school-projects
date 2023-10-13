// CS 3305 Assignment 1
// Author: Shaylan Pratt

#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>
#include <string.h>
#include <sys/wait.h>

int main(int argc, char **argv){
    pid_t pid;
    printf("parent (PID %d): process started\n", getpid());
    printf("parent (PID %d): forking child_1\n", getpid());
    pid=fork();   // create child 1
    if(pid<0){
        printf("First fork unsuccessful");
    }
    else if (pid==0){    //child process 1
        printf("parent (PID %d) fork successful for child_1 (PID %d)\n", getppid(), getpid());
        printf("parent (PID %d): waiting for child_1 (PID %d) to complete\n",getppid(), getpid());
        printf("child_1 (PID %d): process started from parent (PID %d)\n", getpid(), getppid());
        pid_t pid2;
        printf("child_1 (PID %d): forking child_1.1\n", getpid());
        pid2=fork();  // create child 1.1
        wait(NULL);
        if(pid2<0){
            printf("Fork 2 unsuccessful");
        }
        else if (pid2==0){  //child process 1.1
            // call external program 
            printf("child_1.1 (PID %d): process started from child_1 (PID %d)\n",getpid(),getppid());
            printf("child_1.1 (PID %d): calling an external program [./external_program1.out]\n",getpid());
            execl("external_program1.out",argv[1],NULL);
        }
        else{
            printf("child_1 (PID %d): completed child_1.1\n",getppid());
            // create child 1.2
            pid_t pid3;
            printf("child_1 (PID %d): forking child_1.2\n", getpid());
            pid3=fork();
            wait(NULL);
            if(pid3<0){
                printf("Fork 3 unsuccessful");
            }
            else if (pid3==0){   //child process 1.2
                printf("child_1.2 (PID %d): process started from child_1 (PID %d)\n",getpid(),getppid());
                printf("child_1.2 (PID %d): calling an external program [./external_program1.out]\n",getpid());
                // call external program
                execl("external_program1.out",argv[2],NULL);
                printf("child_1 (PID %d): completed child_1.2\n",getppid());
            }
            else{
                printf("child_1 (PID %d): completed child_1.2\n",getppid());
            }
        }
    }
    else{
        wait(NULL);
        // create child 2
        pid_t pid4;
        printf("parent (PID %d): forking child_2\n", getpid());
        pid4=fork();
        wait(NULL);
        if(pid4<0){
            printf("Fork 4 unsuccessful");
        }
        else if(pid4==0) { //child process 2
            printf("parent (PID %d): fork successful for child_2 (PID %d)\n", getppid(), getpid());
            printf("child_2 (PID %d): process started from parent (PID %d)\n", getpid(),getppid());
            printf("child_2 (PID %d): calling an external prgram [./external_program2.out]\n", getpid());
            execl("external_program2.out",argv[3],NULL);
        } 
        else{
            printf("parent (PID %d): completed parent\n", getpid());
        }
    }

}
