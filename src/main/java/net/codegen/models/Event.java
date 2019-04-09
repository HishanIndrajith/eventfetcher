package net.codegen.models;


import javax.persistence.*;

@Entity
public abstract class Event
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int eventId;
}
