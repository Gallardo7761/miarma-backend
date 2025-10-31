package net.miarma.api.backlib.exceptions;

/**
 * Excepción lanzada cuando el tipo de medio (MIME type) de una solicitud no es compatible
 * con lo que el servidor puede procesar. Esto puede ocurrir, por ejemplo, cuando se envía
 * un tipo de contenido no soportado en una solicitud HTTP.
 *
 * <p>Esta excepción es de tipo {@link RuntimeException}, por lo que no es necesario
 * declararla explícitamente en los métodos que la lanzan.</p>
 *
 * @author José Manuel Amador Gallardo
 */
public class UnsupportedMediaTypeException extends RuntimeException {

	private static final long serialVersionUID = 1829890832415237556L;

	/**
	 * Crea una nueva instancia de {@code UnsupportedMediaTypeException} con un mensaje descriptivo.
	 *
	 * @param message El mensaje que describe el error.
	 */
	public UnsupportedMediaTypeException(String message) {
		super(message);
	}

	/**
	 * Crea una nueva instancia de {@code UnsupportedMediaTypeException} con un mensaje y una causa.
	 *
	 * @param message El mensaje que describe el error.
	 * @param cause La causa original de esta excepción.
	 */
	public UnsupportedMediaTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Crea una nueva instancia de {@code UnsupportedMediaTypeException} con una causa.
	 *
	 * @param cause La causa original de esta excepción.
	 */
	public UnsupportedMediaTypeException(Throwable cause) {
		super(cause);
	}

}
