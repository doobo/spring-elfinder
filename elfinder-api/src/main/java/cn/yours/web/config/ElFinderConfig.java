package cn.yours.web.config;

import cn.yours.elfinder.ElFinderConstants;
import cn.yours.elfinder.command.CommandFactory;
import cn.yours.elfinder.configuration.ElfinderConfigurationUtils;
import cn.yours.elfinder.core.Volume;
import cn.yours.elfinder.core.VolumeSecurity;
import cn.yours.elfinder.core.impl.DefaultVolumeSecurity;
import cn.yours.elfinder.core.impl.SecurityConstraint;
import cn.yours.elfinder.param.Node;
import cn.yours.elfinder.service.ElfinderStorage;
import cn.yours.elfinder.service.ElfinderStorageFactory;
import cn.yours.elfinder.service.VolumeSources;
import cn.yours.elfinder.service.impl.DefaultElfinderStorage;
import cn.yours.elfinder.service.impl.DefaultElfinderStorageFactory;
import cn.yours.elfinder.service.impl.DefaultThumbnailWidth;
import cn.yours.elfinder.support.locale.LocaleUtils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.*;

@Configuration
public class ElFinderConfig {

    @Resource
    private ElfinderConfiguration elfinderConfiguration;

    @Bean(name = "commandFactory")
    public CommandFactory getCommandFactory() {
        CommandFactory commandFactory = new CommandFactory();
        commandFactory.setClassNamePattern("cn.yours.elfinder.command.%sCommand");
        return commandFactory;
    }
    
    @Bean(name = "elfinderStorageFactory")
    public ElfinderStorageFactory getElfinderStorageFactory() {
        DefaultElfinderStorageFactory elfinderStorageFactory = new DefaultElfinderStorageFactory();
        elfinderStorageFactory.setElfinderStorage(getElfinderStorage());
        return elfinderStorageFactory;
    }

    @Bean(name = "elfinderStorage")
    public ElfinderStorage getElfinderStorage() {
        DefaultElfinderStorage defaultElfinderStorage = new DefaultElfinderStorage();
        // creates thumbnail
        DefaultThumbnailWidth defaultThumbnailWidth = new DefaultThumbnailWidth();
        defaultThumbnailWidth.setThumbnailWidth(elfinderConfiguration.getThumbnail().getWidth().intValue());

        // creates volumes, volumeIds, volumeLocale and volumeSecurities
        Character defaultVolumeId = 'A';
        List<Node> elfinderConfigurationVolumes = elfinderConfiguration.getVolumes();
        List<Volume> elfinderVolumes = new ArrayList<>(elfinderConfigurationVolumes.size());
        Map<Volume, String> elfinderVolumeIds = new HashMap<>(elfinderConfigurationVolumes.size());
        Map<Volume, Locale> elfinderVolumeLocales = new HashMap<>(elfinderConfigurationVolumes.size());
        List<VolumeSecurity> elfinderVolumeSecurities = new ArrayList<>();

        // creates volumes
        for (Node elfinderConfigurationVolume : elfinderConfigurationVolumes) {
            final String alias = elfinderConfigurationVolume.getAlias();
            String path = elfinderConfigurationVolume.getPath();
            final String source = elfinderConfigurationVolume.getSource();
            if(VolumeSources.FILESYSTEM.name().equalsIgnoreCase(source)){
                path = ElfinderConfigurationUtils.createDirIfAbsent(path.trim()).getAbsolutePath();
                elfinderConfigurationVolume.setPath(path);
            }
            final String locale = elfinderConfigurationVolume.getLocale();
            final boolean isLocked = elfinderConfigurationVolume.getConstraint().isLocked();
            final boolean isReadable = elfinderConfigurationVolume.getConstraint().isReadable();
            final boolean isWritable = elfinderConfigurationVolume.getConstraint().isWritable();

            // creates new volume
            Volume volume = VolumeSources.of(source).newInstance(alias, path, elfinderConfigurationVolume);
            
            elfinderVolumes.add(volume);
            elfinderVolumeIds.put(volume, Character.toString(defaultVolumeId));
            elfinderVolumeLocales.put(volume, LocaleUtils.toLocale(locale));

            // creates security constraint
            SecurityConstraint securityConstraint = new SecurityConstraint();
            securityConstraint.setLocked(isLocked);
            securityConstraint.setReadable(isReadable);
            securityConstraint.setWritable(isWritable);

            // creates volume pattern and volume security
            final String volumePattern = Character.toString(defaultVolumeId) + ElFinderConstants.ELFINDER_VOLUME_SERCURITY_REGEX;
            elfinderVolumeSecurities.add(new DefaultVolumeSecurity(volumePattern, securityConstraint));

            // prepare next volumeId character
            defaultVolumeId++;
        }

        defaultElfinderStorage.setThumbnailWidth(defaultThumbnailWidth);
        defaultElfinderStorage.setVolumes(elfinderVolumes);
        defaultElfinderStorage.setVolumeIds(elfinderVolumeIds);
        defaultElfinderStorage.setVolumeLocales(elfinderVolumeLocales);
        defaultElfinderStorage.setVolumeSecurities(elfinderVolumeSecurities);

        return defaultElfinderStorage;
    }

}
