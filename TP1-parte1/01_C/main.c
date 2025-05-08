#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

#define PROCESS_A 'A'
#define PROCESS_B 'B'
#define PROCESS_C 'C'
#define PROCESS_D 'D'
#define PROCESS_E 'E'
#define PROCESS_F 'F'
#define PROCESS_G 'G'
#define PROCESS_H 'H'
#define PROCESS_I 'I'

#define SLEEP_TIME_10 10
#define SLEEP_TIME_20 20


void error(char letter)
{
    printf("Error de creacion en el proceso %c\n", letter);
    exit(EXIT_FAILURE);
}

void process(char letter)
{
    pid_t pid = getpid();
    pid_t ppid = getppid();
    printf("Proceso %c (PID: %d) (PPID: %d)\n", letter, pid, ppid);

    if (letter == PROCESS_A)
    {
        pid_t b = fork();
        if (b < 0)
        {
            error(PROCESS_B);
        }
        else if (b == 0)
        {
            process(PROCESS_B);
            exit(EXIT_SUCCESS);
        }
        sleep(SLEEP_TIME_20);
        wait(NULL);
    }
    else if (letter == PROCESS_B)
    {
        pid_t c = fork();
        if (c < 0)
        {
            error(PROCESS_C);
        } else if (c == 0)
        {
            process(PROCESS_C);
            sleep(SLEEP_TIME_10);
            exit(EXIT_SUCCESS);
        }

        pid_t d = fork();
        if (d < 0)
        {
            error(PROCESS_D);
        } else if (d == 0)
        {
            process(PROCESS_D);
            sleep(SLEEP_TIME_10);
            exit(EXIT_SUCCESS);
        }

        sleep(SLEEP_TIME_20);
        wait(NULL);
        wait(NULL);
    }
    else if (letter == PROCESS_D)
    {
        pid_t f = fork();
        if (f < 0)
        {
            error(PROCESS_F);
        } else if (f == 0)
        {
            process(PROCESS_F);
            sleep(SLEEP_TIME_10);
            exit(EXIT_SUCCESS);
        }

        pid_t g = fork();
        if (g < 0)
        {
            error(PROCESS_F);
        } else if (g == 0)
        {
            process(PROCESS_F);
            sleep(SLEEP_TIME_10);
            exit(EXIT_SUCCESS);
        }

        sleep(SLEEP_TIME_20);
        wait(NULL);
        wait(NULL);
    }
    else if (letter == PROCESS_C)
    {
        pid_t e = fork();
        if (e < 0)
        {
            error(PROCESS_E);
        } else if (e == 0)
        {
            process(PROCESS_E);
            sleep(SLEEP_TIME_10);
            exit(EXIT_SUCCESS);
        }
        wait(NULL);
    }
    else if (letter == PROCESS_E)
    {
        pid_t h = fork();
        if (h < 0)
        {
            error(PROCESS_H);
        } else if (h == 0)
        {
            process(PROCESS_H);
            sleep(SLEEP_TIME_10);
            exit(EXIT_SUCCESS);
        }

        pid_t i = fork();
        if (i < 0)
        {
            error(PROCESS_I);
        } else if (i == 0)
        {
            process(PROCESS_I);
            sleep(SLEEP_TIME_10);
            exit(EXIT_SUCCESS);
        }

        wait(NULL);
        wait(NULL);
    }
    else
    {
        sleep(10);
    }
}

int main()
{
    process(PROCESS_A);
    return 0;
}
