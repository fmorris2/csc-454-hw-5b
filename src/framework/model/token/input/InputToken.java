package framework.model.token.input;

import framework.model.Model;
import framework.model.token.Token;

public class InputToken<T extends Model> implements Token {
	protected T model;
}
