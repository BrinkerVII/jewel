package net.brinkervii.jewel.core.work.workers.sass;

import io.bit3.jsass.CompilationException;
import io.bit3.jsass.Compiler;
import io.bit3.jsass.Options;
import io.bit3.jsass.Output;
import io.bit3.jsass.importer.Import;
import io.bit3.jsass.importer.Importer;
import lombok.extern.slf4j.Slf4j;
import net.brinkervii.common.BucketOfShame;
import net.brinkervii.jewel.core.FileAccumulator;
import net.brinkervii.jewel.core.config.JewelConfiguration;
import net.brinkervii.jewel.core.document.Stylesheet;
import net.brinkervii.jewel.core.exception.NotADirectoryException;
import net.brinkervii.jewel.core.work.driver.DefaultJewelWorkerChain;
import net.brinkervii.jewel.core.work.driver.JewelWorker;
import net.brinkervii.jewel.util.FileUtil;
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

	public SassCompilerWorker(DefaultJewelWorkerChain chain) {
		super(chain);
	}

	@Override
	public void run() {
		log.info("Running SASS compiler");

		final JewelConfiguration config = chain.getContext().config();
		FileAccumulator accumulator = new FileAccumulator(config.getThemeLocation(), config.getSourceLocation());
		FileAccumulator partialsAccumulator = accumulator.duplicate();

		Collection<Importer> importers = Collections.singleton((url, previous) -> {
			final URI importUri = previous.getImportUri();

			if ("~/sass_variables".equals(url)) {
				try {
					return importSassVariablesFromConfig(new URI(url));
				} catch (URISyntaxException e) {
					BucketOfShame.accept(e);
					return null;
				}
			}

			File previousFile = new File(importUri.toString());

			String[] exploded = url.split("/");
			exploded[exploded.length - 1] = "_" + exploded[exploded.length - 1] + ".scss";
			final File file = new File(previousFile.getParentFile().toString() + "/" + String.join("/", exploded));
			final boolean exists = file.exists();

			log.info(String.format("Processed import '%s', the file %s", url, exists ? "exists" : "does not exist"));

			try {
				return Collections.singleton(new Import(file.toURI(), file.getAbsoluteFile().toURI(), IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8)));
			} catch (IOException e) {
				BucketOfShame.accept(e);
			}

			return null;
		});
		options.setImporters(importers);

		try {
			partialsAccumulator.accumulate(new SassPartialFilenameFilter());

			accumulator.accumulate(new SassFilenameFilter()).getFiles().forEach((root, files) -> {
				for (File file : files) {
					final FileInputStream fileInputStream;
					try {
						fileInputStream = new FileInputStream(file);
						final String scss = IOUtils.toString(fileInputStream, StandardCharsets.UTF_8);
						fileInputStream.close();

						final Compiler compiler = new Compiler();
						final Output output = compiler.compileFile(file.toURI(), new URI("out.css"), options);

						final Stylesheet stylesheet = Stylesheet.withContent(root, output.getCss());
						stylesheet.setSourceMap(output.getSourceMap());
						stylesheet.setSourceFile(FileUtil.changeExtension(file, "css"));

						chain.getContext().stylesheet(stylesheet);
						log.info(String.format("Added compiled SASS file %s", file.getName()));
					} catch (IOException | CompilationException | URISyntaxException e) {
						BucketOfShame.accept(e);
					}

				}
			});
		} catch (FileNotFoundException | NotADirectoryException e) {
			BucketOfShame.accept(e);
		}
	}

	private Collection<Import> importSassVariablesFromConfig(URI importUri) {
		final String compiledVars = chain.getContext().config().compileSassVariables();

		return Collections.singleton(new Import(
			importUri,
			importUri,
			compiledVars
		));
	}
}
