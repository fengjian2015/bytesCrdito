package pers.spj.adapter.androidx.listener;

import java.util.List;

import pers.spj.adapter.androidx.service.ViewHolderService;

/**
 * @author supeijin
 */
public interface OnBindViewHolderListener<T> {
	void onBindViewHolder(ViewHolderService service, List<T> list, T item, int postion);
}
