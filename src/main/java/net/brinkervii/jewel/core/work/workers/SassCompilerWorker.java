package net.brinkervii.jewel.core.work.workers;

import io.bit3.jsass.CompilationException;
import io.bit3.jsass.Compiler;
import io.bit3.jsass.Options;
import io.bit3.jsass.Output;
import io.bit3.jsass.importer.Import;
import io.bit3.jsass.importer.Importer;
import lombok.extern.slf4j.Slf4j;
import net.brinkervii.jewel.core.FileAccumulator;
import net.brinkervii.jewel.core.Stylesheet;
import net.brinkervii.jewel.core.exception.NotADirectoryException;
import net.brinkervii.jewel.core.work.driver.DefaultJewelWorkerChain;
import net.brinkervii.jewel.core.work.driver.JewelWorker;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;

@Slf4j
public final class SassCompilerWorker extends JewelWorker {
	private Options options = new Options();

	public SassCompilerWorker(DefaultJewelWorkerChain defaultJewelWorkerChain) {
		super(defaultJewelWorkerChain);
	}

	@Override
	public void run() {
		FileAccumulator accumulator = new FileAccumulator("theme/style");
		FileAccumulator partialsAccumulator = new FileAccumulator("theme/style");

		Collection<Importer> importers = Collections.singleton((url, previous) -> {
			final URI importUri = previous.getImportUri();
			File previousFile = new File(importUri.toString());

			String[] exploded = url.split("/");
			exploded[exploded.length - 1] = "_" + exploded[exploded.length - 1] + ".scss";
			final File file = new File(previousFile.getParentFile().toString() + "/" + String.join("/", exploded));
			final boolean exists = file.exists();

			log.info(String.format("Processed import '%s', the file %s", url, exists ? "exists" : "does not exist"));

			try {
				return Collections.singleton(new Import(file.toURI(), file.getAbsoluteFile().toURI(), IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8)));
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		});
		options.setImporters(importers);

		try {
			partialsAccumulator.accumulate(new SassPartialFilenameFilter());

			for (File file : accumulator.accumulate(new SassFilenameFilter()).getFiles()) {
				final FileInputStream fileInputStream = new FileInputStream(file);
				final String scss = IOUtils.toString(fileInputStream, StandardCharsets.UTF_8);
				fileInputStream.close();

				final Compiler compiler = new Compiler();
				final Output output = compiler.compileFile(file.toURI(), new URI("out.css"), options);

				final Stylesheet stylesheet = Stylesheet.withContent(output.getCss());
				stylesheet.setSourceMap(output.getSourceMap());
				chain.getContext().stylesheet(stylesheet);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NotADirectoryException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CompilationException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
