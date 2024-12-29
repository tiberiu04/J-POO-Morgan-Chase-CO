package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.entities.User;

import java.util.List;

/**
 * Interface defining the structure of a Command with execute methods.
 */
public interface Command {

	/**
	 * Executes the command with a list of users and an output node.
	 *
	 * @param users  the list of users the command operates on
	 * @param output the JSON array node to store the output
	 */
	void execute(List<User> users, ArrayNode output);
}
