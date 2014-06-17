#pragma once

#include <string>

using namespace std;

class ModelInterface
{
public:
	ModelInterface();
	~ModelInterface();

	virtual string toNetworkString() = 0;

private:

};
