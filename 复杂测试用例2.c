
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
		printf("------------------------------------------------------\n��һ�⣺\n");
		while (flag)
		{
			printf("-----------------------------------------------\n");
			printf("1.����һ������\n");
			printf("2.����һ�������Ƿ����\n");
			printf("3.ɾ��һ������\n");
			printf("����������ѡ��:");
			scanf("%d", &choice);
			printf("-----\n��Ҫ������һ��������1������������0.");
			do
			{
				scanf("%d", &flag);
			} while (flag != 1 && flag != 0);			
		}
		printf("-----------------------------------------------------------\n�ڶ��⣺\n");
		int *tree_array;
		tree_array = (int*)malloc(sizeof(int)*num_all);
		Inorder(head, 0, tree_array);
		printf("����������Ϊ��\n");
		for (int i = 0; i < num_all; i++)
		{
			printf("%d   ", tree_array[i]);
		}
		printf("\n");

		do 
		{
			printf("-----------------------------------------------\n");
			printf("��������Ҫ���ҵ�����\n");
			scanf("%d", &num);
			int num_int = BinSearch1(num, tree_array);
			if (num_int == 0)//����ֵΪ�㣬֤������ʧ��
			{
				printf("%d���ݲ��ڱ����е��С�\n", num);
			}
			else
			{
				printf("%d�������������ݵĵ�%d��Ԫ��\n", num, num_int + 1);
			}
			printf("-----\n��Ҫ�����ڶ���������1������������0.");
			do
			{
				scanf("%d", &flag);
			} while (flag != 1 && flag != 0);
		} while (flag);
		
		printf("-----------------------------------------------------------\n�����⣺\n");
		do
		{
			printf("-----------------------------------------------\n");
			printf("1.ð������\n");
			printf("2.��������\n");
			printf("3.ѡ������\n");
			printf("4.��������\n");
			printf("5.��ӡ���������������Ԫ��\n");
			printf("����������ѡ��\n");
			scanf("%d", &choice);
			printf("-----\n��Ҫ����������������1������������0.");
			do
			{
				scanf("%d", &flag);
			} while (flag != 1 && flag != 0);
		} while (flag);
		printf("-----\n��Ҫ����������������1������������0.");
		do
		{
			scanf("%d", &flag);
		} while (flag != 1 && flag != 0);
	}while (flag);
