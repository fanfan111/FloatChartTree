
	TREE* head;
	head =CreateTree();
	srand(time(NULL));

	int *big_array;
	big_array = (int*)malloc(sizeof(int)*N);

	for (int i = 0; i < N; i++)
	{
		big_array[i] = rand() % 100000;
	}

	double time[4];
	int num, choice,flag = 1;
	clock_t start, finish;
	do
	{
		int tempt,i;
		flag = 1;
		printf("------------------------------------------------------\n第一题：\n");
		while (flag)
		{
			printf("-----------------------------------------------\n");
			printf("1.插入一个数据\n");
			printf("2.查找一个数据是否存在\n");
			printf("3.删除一个数据\n");
			printf("请输入您的选择:");
			scanf("%d", &choice);
			printf("-----\n如要继续第一题请输入1，否则请输入0.");
			do
			{
				scanf("%d", &flag);
			} while (flag != 1 && flag != 0);			
		}
		printf("-----------------------------------------------------------\n第二题：\n");
		int *tree_array;
		tree_array = (int*)malloc(sizeof(int)*num_all);
		Inorder(head, 0, tree_array);
		printf("中序遍历结果为：\n");
		for (int i = 0; i < num_all; i++)
		{
			printf("%d   ", tree_array[i]);
		}
		printf("\n");

		do 
		{
			printf("-----------------------------------------------\n");
			printf("请输入您要查找的数据\n");
			scanf("%d", &num);
			int num_int = BinSearch1(num, tree_array);
			if (num_int == 0)//返回值为零，证明查找失败
			{
				printf("%d数据不在本序列当中。\n", num);
			}
			else
			{
				printf("%d数据是这组数据的第%d个元素\n", num, num_int + 1);
			}
			printf("-----\n如要继续第二题请输入1，否则请输入0.");
			do
			{
				scanf("%d", &flag);
			} while (flag != 1 && flag != 0);
		} while (flag);
		
		printf("-----------------------------------------------------------\n第三题：\n");
		do
		{
			printf("-----------------------------------------------\n");
			printf("1.冒泡排序\n");
			printf("2.插入排序\n");
			printf("3.选择排序\n");
			printf("4.快速排序\n");
			printf("5.打印排序后数组中所有元素\n");
			printf("请输入您的选择\n");
			scanf("%d", &choice);
			printf("-----\n如要继续第三题请输入1，否则请输入0.");
			do
			{
				scanf("%d", &flag);
			} while (flag != 1 && flag != 0);
		} while (flag);
		printf("-----\n如要继续本程序请输入1，否则请输入0.");
		do
		{
			scanf("%d", &flag);
		} while (flag != 1 && flag != 0);
	}while (flag);
