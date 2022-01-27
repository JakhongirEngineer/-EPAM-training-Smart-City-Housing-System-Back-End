package com.epam.smartcityhousingsystem.services;

import com.epam.smartcityhousingsystem.entities.AdvertisementArchive;
import com.epam.smartcityhousingsystem.exceptions.AdvertisementArchiveNotFoundException;
import com.epam.smartcityhousingsystem.repositories.AdvertisementArchiveRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@AllArgsConstructor
@Service
public class AdvertisementArchiveService {
    private final AdvertisementArchiveRepository advertisementArchiveRepository;

    /**
     *
     * @param pageNumber represents page number for pagination
     * @param pageSize represents page size for pagination
     * @param sortBy represents a field that is used to sort for pagination
     * @return List of AdvertisementArchive s
     * If there is not advertisement archives, the method throws
     * an <code>AdvertisementArchiveNotFoundException</code> exception
     */
    public List<AdvertisementArchive> getAllAdvertisementArchives(int pageNumber, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize, Sort.by(sortBy));
        Page<AdvertisementArchive> advertisementArchivePage = advertisementArchiveRepository.findAll(pageable);
        if (advertisementArchivePage.hasContent()){
            return advertisementArchivePage.getContent();
        } else {
            throw new AdvertisementArchiveNotFoundException("there are not advertisement archives");
        }
    }

    /**
     *
     * @param advertisementArchiveUUID is used to uniquely identify advertisement archive.
     * @return AdvertisementArchive
     * method throws an <code>AdvertisementArchiveNotFoundException</code> exception
     * if there is not an advertisement archive with a given advertisementArchiveUUID
     */
    public AdvertisementArchive getAdvertisementArchive(String advertisementArchiveUUID) {
        return advertisementArchiveRepository.findByUuid(advertisementArchiveUUID)
                .orElseThrow(() -> new AdvertisementArchiveNotFoundException("AdvertisementArchive not found"));
    }

    /**
     *
     * @param advertisementArchiveUUID is used to uniquely identify advertisement archive.
     * @return boolean that specifies if an advertisement archive is deleted or not.
     */
    @Transactional
    public boolean deleteAdvertisementArchive(String advertisementArchiveUUID) {
       return advertisementArchiveRepository.deleteByUuid(advertisementArchiveUUID) > 0;
    }
}
