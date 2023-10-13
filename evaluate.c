// CS2211b 2023
// Assignment 3
// Shaylan Pratt
// 251225611
// spratt28
// March 1, 2023

#include <stdio.h>
#include <stdlib.h>


char get_op(void){
    char op;
    scanf("%c", &op);   // read the char from the stdin
    while(op==' '){     // this loop is to not skip end of line
        scanf("%c", &op);
    }
    return op;
}

float get_num(void){
    float num;
    scanf(" %f", &num);   // read the number from the stdin
    return num;
}


float md_sub_exp(float sub_exp){
    char next_op;
    float next_num;
    float next_sub_exp;
    next_op = get_op();
    if(next_op != '\n'&& next_op != '+' && next_op != '-' && next_op != '*' && next_op != '/'){   // checks for valid operation input
        printf("Invalid input\n"); 
        exit(EXIT_FAILURE);
    }
    if(next_op == '\n'|| next_op == '+' || next_op == '-'){    // if the next operation in not * or / leave the expression alone
        ungetc(next_op,stdin);   // push the char back to the stdin
        return sub_exp;
    }else{
        next_num = get_num();
        if(next_op == '*'){     // perform the multiplication
            next_sub_exp = sub_exp * next_num;
        }
        if(next_op == '/'){    // perform the division
            next_sub_exp = sub_exp/next_num;
        }
        return md_sub_exp(next_sub_exp);  // recursive call 
    }
}

float md_exp(void){    // this function returns the next md_exp
    float m = get_num();  // get the first number in the expression
    return md_sub_exp(m);
}

float sim_sub_exp(float sub_exp){  // this function uses recursion to return the value of the expression
    char next_op;
    float next_sub_exp;
    float next_md_exp;
    next_op = get_op();
    if(next_op == '\n'){    // the end of the expression
        ungetc(next_op,stdin);
        return sub_exp;
    }else{
        next_md_exp = md_exp();  // get the md_exp using the function
        if(next_op=='+'){  // perform the addition 
            next_sub_exp = sub_exp+next_md_exp;
        }
        if(next_op=='-'){ // perform the subtraction 
            next_sub_exp = sub_exp-next_md_exp;
        }
        return sim_sub_exp(next_sub_exp);  // recursive call 
    }
}

float sim_exp(void){   // this function returns the next simp_exp
    float m = md_exp();
    return sim_sub_exp(m);
}


void main(){
    int repeat = 1;
    while(repeat == 1){  // while loop to continue if the user wants to 
        char y;
        printf("Type your expression:\n");
        float num = md_exp();   // get the md_exp
        printf("%f\n",sim_sub_exp(num)); // use the funtion to evaluate the md_exp
        printf("Do you want to continue? Y for yes, N for no\n"); // ask user if they wish to continue 
        scanf(" %c", &y); 
        if(y=='N'){  // terminate if no
            repeat = 0;
        }

    }
    
}